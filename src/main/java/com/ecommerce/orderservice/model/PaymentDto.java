package com.ecommerce.orderservice.model;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Class representing a payment in e-commerce application.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

	@ApiModelProperty(notes = "Unique identifier of the Order.", example = "1")
	private Long id;

	@ApiModelProperty(notes = "Status of the Payment.", example = "PENDING", required = true)
	private PaymentStatus status;

	@ApiModelProperty(notes = "Unique identifier of the Order.", example = "1", required = true)
	@NotNull
	private Long orderId;

	public PaymentDto(PaymentStatus status, @NotNull Long orderId) {
		super();
		this.status = status;
		this.orderId = orderId;
	}

}
