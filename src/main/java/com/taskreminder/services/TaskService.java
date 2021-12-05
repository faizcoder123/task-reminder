package com.taskreminder.services;

import com.taskreminder.constants.Constants;
import com.taskreminder.dto.PaginationDTO;
import com.taskreminder.dto.SearchCriteriaDTO;
import com.taskreminder.dto.TaskESDTO;
import com.taskreminder.entities.Status;
import com.taskreminder.entities.TaskEntity;
import com.taskreminder.es.SearchQueryConverter;
import com.taskreminder.handler.ApiRequestException;
import com.taskreminder.repository.TaskRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public List<TaskEntity> getAllTasks(Principal principal) {
        List<TaskEntity> tasks = taskRepository.findByOwnerEmail(principal.getName());
        if(tasks.isEmpty()){
            throw new ApiRequestException("No Tasks found for this User");
        }
        return tasks;
    }

    public List<TaskEntity> getAllTasksOfUser(String mail) {
        List<TaskEntity> tasks = taskRepository.findByOwnerEmail(mail);
        if(tasks.isEmpty()){
            //log("No Tasks found for this User");
        }
        return tasks;
    }

    public void deleteTask(long id, Principal principal) throws IOException {
        getTask(id, principal);
        taskRepository.deleteById(id);
    }

    public void deleteAllTaskOfUser(String mail) throws IOException {
        for(TaskEntity task: getAllTasksOfUser(mail)){
            taskRepository.deleteById(task.getId());
        }
    }

    public TaskEntity addOrUpdateTask(TaskEntity task, Principal principal) {
        if(task.getDeadline().compareTo(ZonedDateTime.now()) < 0){
            throw new ApiRequestException("Deadline should be greater than current time");
        }
        task.setOwnerEmail(principal.getName());
        if(Objects.isNull(task.getStatus())){
            task.setCreatedTime(ZonedDateTime.parse(Constants.FORMATTER.format(ZonedDateTime.now()), Constants.FORMATTER));
            task.setStatus(Status.STARTED);
        }
        task.setModifiedTime(ZonedDateTime.parse(Constants.FORMATTER.format(ZonedDateTime.now()), Constants.FORMATTER));
        taskRepository.save(task);
        return task;
    }

    public TaskEntity updateTask(TaskEntity task, long id, Principal principal) {
        TaskEntity taskFound = getTask(id, principal);
         if(!task.getOwnerEmail().equals(taskFound.getOwnerEmail())){
             throw new ApiRequestException("No such task found for this user");
         }
         task.setId(id);
         addOrUpdateTask(task, principal);
         return task;
    }

    public TaskEntity getTask(long id, Principal principal) {
        Optional<TaskEntity> task = taskRepository.findById(id);
        if(task.isPresent()){
            if(!principal.getName().equals(task.get().getOwnerEmail())) throw new ApiRequestException("Authentication failed");
            return task.get();
        }
        else {
            throw new ApiRequestException("task not found");
        }
    }

    public List<TaskESDTO> search(PaginationDTO pagination, List<SearchCriteriaDTO> searchCriteria, String userMail) {
        if (CollectionUtils.isEmpty(searchCriteria)) {
            throw new ApiRequestException("Search criteria not provided");
        }
        searchCriteria.add(new SearchCriteriaDTO("ownerEmail", "=", userMail));
        return findPOsInES(pagination, searchCriteria);
    }

    public List<TaskESDTO> findPOsInES(PaginationDTO pagination, List<SearchCriteriaDTO> searchCriteria)  {
        BoolQueryBuilder query = SearchQueryConverter.getQuery(searchCriteria);
        Query nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(query).withPageable(pagination.pageable((pagination)))
                .build();
        try {
            SearchHits<TaskESDTO> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, TaskESDTO.class);
            return searchHits.get().map(SearchHit::getContent).collect(Collectors.toList());
        }
        catch (Exception e){
            throw new ApiRequestException("Some ERROR Occurred while processing Request");
        }

    }
}
