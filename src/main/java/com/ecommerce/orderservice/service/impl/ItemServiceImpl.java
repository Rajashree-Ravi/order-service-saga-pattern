package com.ecommerce.orderservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.orderservice.entity.Item;
import com.ecommerce.orderservice.repository.ItemRepository;
import com.ecommerce.orderservice.service.ItemService;
import com.ecommerce.orderservice.model.ItemDto;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public List<ItemDto> getAllItems() {
		List<ItemDto> items = new ArrayList<>();
		itemRepository.findAll().forEach(item -> {
			items.add(mapper.map(item, ItemDto.class));
		});
		return items;
	}

	@Override
	public ItemDto getItemById(long id) {
		Optional<Item> item = itemRepository.findById(id);
		return (item.isPresent() ? mapper.map(item.get(), ItemDto.class) : null);
	}

	@Override
	public ItemDto createItem(ItemDto itemDto) {
		Item item = mapper.map(itemDto, Item.class);
		return mapper.map(itemRepository.save(item), ItemDto.class);
	}

	@Override
	public ItemDto updateItem(long id, ItemDto itemDto) {

		Optional<Item> updatedItem = itemRepository.findById(id).map(existingItem -> {
			Item item = mapper.map(itemDto, Item.class);
			return itemRepository.save(existingItem.updateWith(item));
		});

		return (updatedItem.isPresent()) ? mapper.map(updatedItem.get(), ItemDto.class) : null;
	}

	@Override
	public void deleteItem(long id) {
		if (getItemById(id) != null)
			itemRepository.deleteById(id);
	}

}
