package com.example.project.ecomm.ECommerce.validatorobjects;

import lombok.Data;

@Data
public class StatusDto
{
    private int orderProductId;
    private String fromStatus;
    private String toStatus;
}
