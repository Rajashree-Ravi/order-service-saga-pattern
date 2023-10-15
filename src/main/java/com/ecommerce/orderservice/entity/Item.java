package com.ecommerce.orderservice.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private int quantity;

	@NotNull
	private BigDecimal subTotal;

	@NotNull
	private Long productId;

	private Long inventoryId;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "items")
	@JsonIgnore
	private List<Order> orders;

	public Item(Long id, @NotNull int quantity, @NotNull BigDecimal subTotal, @NotNull Long productId,
			Long inventoryId) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.subTotal = subTotal;
		this.productId = productId;
		this.inventoryId = inventoryId;
	}

	public Item updateWith(Item item) {
		return new Item(this.id, item.quantity, item.subTotal, item.productId, item.inventoryId);
	}

}
