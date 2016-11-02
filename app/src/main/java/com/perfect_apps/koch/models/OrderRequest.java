package com.perfect_apps.koch.models;

/**
 * Created by mostafa_anter on 11/2/16.
 */

public class OrderRequest {
    private String title;
    private String detail;
    private String cost;
    private String row_hash;

    private String status;
    private String updated_at;

    private String providerName;
    private String providerImage;

    private String clientName;
    private String clientImage;

    public OrderRequest(String title, String detail, String cost, String row_hash, String status, String updated_at, String providerName, String providerImage, String clientName, String clientImage) {
        this.title = title;
        this.detail = detail;
        this.cost = cost;
        this.row_hash = row_hash;
        this.status = status;
        this.updated_at = updated_at;
        this.providerName = providerName;
        this.providerImage = providerImage;
        this.clientName = clientName;
        this.clientImage = clientImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getRow_hash() {
        return row_hash;
    }

    public void setRow_hash(String row_hash) {
        this.row_hash = row_hash;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderImage() {
        return providerImage;
    }

    public void setProviderImage(String providerImage) {
        this.providerImage = providerImage;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientImage() {
        return clientImage;
    }

    public void setClientImage(String clientImage) {
        this.clientImage = clientImage;
    }
}
