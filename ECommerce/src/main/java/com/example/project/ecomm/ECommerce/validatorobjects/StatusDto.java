package com.example.project.ecomm.ECommerce.validatorobjects;

import com.example.project.ecomm.ECommerce.entities.Order.OrderStatusEnums.FromStatus;
import com.example.project.ecomm.ECommerce.entities.Order.OrderStatusEnums.ToStatus;
import lombok.Data;

@Data
public class StatusDto
{
    private int orderProductId;
    private FromStatus fromStatus;
    private ToStatus toStatus;
}
