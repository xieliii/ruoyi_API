package com.ruoyi.workflow.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Axel on 2023/6/12 15:13
 *
 * @author Axel
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ag_workflow_assignee")
public class AgWorkflowAssigneeEntity extends StandardBaseEntity{
    @TableId("id")
    private String id;
    @TableField("process_key")
    private String processKey;
    @TableField("assignee")
    private String assignee;
    @TableField("task_node_key")
    private String taskNodeKey;
    @TableField("process_instance_id")
    private String processInstanceId;
    @TableField("is_multi")
    private Integer isMulti;
}
