package com.cgvsu.table_models_cell;

public class ColumnModel {
    private String modelName;
    private Integer modelId;
    public ColumnModel(String modelName, Integer modelId) {
        this.modelName = modelName;
        this.modelId = modelId;
    }


    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }
}
