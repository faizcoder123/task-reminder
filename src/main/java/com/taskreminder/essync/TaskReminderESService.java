package com.taskreminder.essync;

import com.taskreminder.entities.TaskEntity;
import com.taskreminder.util.DateUtil;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Configuration
public class TaskReminderESService {

    @Value("${elasticsearch.index}")
    protected String elasticsearchIndex;

    @Autowired
    @Qualifier("highLevelClient")
    protected RestHighLevelClient restClient;

    public void onUpdateRequest(TaskEntity task) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(elasticsearchIndex,"_doc" , String.valueOf(task.getId()));
        updateRequest.scriptedUpsert(true);
        updateRequest.script(updateScript(task));
        updateRequest.retryOnConflict(10);
        Map<String, Object> source = new HashMap<>();
        source.put("id", task.getId());
        updateRequest.upsert(source);
        restClient.update(updateRequest, RequestOptions.DEFAULT);

    }

    private Script updateScript(TaskEntity doc) {
        String script = "if(params.subject!=null){ctx._source.subject=params.subject;}"+
                "if(params.created_time!=null){ctx._source.created_time=params.created_time;}"+
                "if(params.deadline!=null){ctx._source.deadline=params.deadline;}"+
                "if(params.modified_time!=null){ctx._source.modified_time=params.modified_time;}" +
                "if(params.status!=null){ctx._source.status=params.status;}" +
                "if(params.description!=null){ctx._source.description=params.description;}" +
                "if(params.owner_email!=null){ctx._source.owner_email=params.owner_email;}";

        Map<String, Object> params = new HashMap<>();
        if(!doc.getSubject().isEmpty()) {params.put("subject", doc.getSubject());}
        if(doc.getOwnerEmail()!=null) {params.put("owner_email", doc.getOwnerEmail());}
        if(doc.getStatus()!=null) {params.put("status", doc.getStatus());}
        if(doc.getCreatedTime()!=null) {params.put("created_time", DateUtil.convertToEsDateFormat(doc.getCreatedTime()));}
        if(doc.getDeadline()!=null) {params.put("dead_line", DateUtil.convertToEsDateFormat(doc.getDeadline()));}
        if(doc.getDescription()!=null) {params.put("description", doc.getDescription());}
        if(doc.getModifiedTime()!=null) {params.put("modified_time", DateUtil.convertToEsDateFormat(doc.getModifiedTime()));}
        return new Script(
                ScriptType.INLINE,
                "painless",
                script,
                params
        );
    }

    public void onDeleteRequest(TaskEntity task) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(elasticsearchIndex,"_doc" , String.valueOf(task.getId()));
        restClient.delete(deleteRequest, RequestOptions.DEFAULT);
    }

}
