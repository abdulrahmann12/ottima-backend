package com.ottima.finishing_tracking.standard_item.service;

import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.Constants;
import com.ottima.finishing_tracking.logging.annotation.LogActivity;
import com.ottima.finishing_tracking.logging.enums.ActionType;
import com.ottima.finishing_tracking.exception.StandardItemAlreadyExistsException;
import com.ottima.finishing_tracking.exception.StandardItemInUseException;
import com.ottima.finishing_tracking.exception.StandardItemNotFoundException;
import com.ottima.finishing_tracking.project.repository.ProjectItemRepository;
import com.ottima.finishing_tracking.standard_item.dto.request.StandardItemRequest;
import com.ottima.finishing_tracking.standard_item.dto.response.StandardItemResponse;
import com.ottima.finishing_tracking.standard_item.entity.StandardItem;
import com.ottima.finishing_tracking.standard_item.mapper.StandardItemMapper;
import com.ottima.finishing_tracking.standard_item.repository.StandardItemRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
public class StandardItemService {

    private final StandardItemRepository standardItemRepository;
    private final StandardItemMapper standardItemMapper;
    private final ProjectItemRepository projectItemRepository;

    @LogActivity(actionType = ActionType.CREATE, entityName = Constants.STANDARD_ITEM_ENTITY, details = Messages.STANDARD_ITEM_CREATED_LOG)
    @Transactional
    @CacheEvict(value = "standardItems", allEntries = true)
    public StandardItemResponse create(@Valid StandardItemRequest request) {
        if (standardItemRepository.existsByNameArOrNameEn(request.getNameAr(), request.getNameEn())) {
            throw new StandardItemAlreadyExistsException();
        }

        StandardItem item = standardItemMapper.toEntity(request);
        StandardItem savedItem = standardItemRepository.save(item);
        return standardItemMapper.toResponse(savedItem);
    }

    @LogActivity(actionType = ActionType.UPDATE, entityName = Constants.STANDARD_ITEM_ENTITY, details = Messages.STANDARD_ITEM_UPDATED_LOG)
    @Transactional
    @CacheEvict(value = "standardItems", allEntries = true)
    public StandardItemResponse update(UUID itemId, @Valid StandardItemRequest request) {
        StandardItem item = getEntityById(itemId);

        if (standardItemRepository.existsByNameAndIdNot(request.getNameAr(), request.getNameEn(), itemId)) {
            throw new StandardItemAlreadyExistsException();
        }

        standardItemMapper.updateEntityFromRequest(request, item);
        StandardItem updatedItem = standardItemRepository.save(item);
        return standardItemMapper.toResponse(updatedItem);
    }

    @Cacheable(value = "standardItems", key = "#itemId")
    public StandardItemResponse getById(UUID itemId) {
        return standardItemMapper.toResponse(getEntityById(itemId));
    }

    public Page<StandardItemResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return standardItemRepository.findAll(pageable)
                .map(standardItemMapper::toResponse);
    }

    @LogActivity(actionType = ActionType.DELETE, entityName = Constants.STANDARD_ITEM_ENTITY, details = Messages.STANDARD_ITEM_DELETED_LOG)
    @Transactional
    @CacheEvict(value = "standardItems", allEntries = true)
    public void delete(UUID itemId) {
        StandardItem item = getEntityById(itemId);

        if (projectItemRepository.existsByStandardItem_ItemId(itemId)) {
            throw new StandardItemInUseException();
        }
        standardItemRepository.delete(item);
    }

    @Cacheable(value = "standardItems", key = "'search-' + (#keyword != null ? #keyword : 'none') + '-' + #page + '-' + #size")
    public Page<StandardItemResponse> getAll(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        String processedKeyword = (keyword != null && !keyword.trim().isEmpty())
                ? keyword.trim()
                : null;

        return standardItemRepository.searchItems(processedKeyword, pageable)
                .map(standardItemMapper::toResponse);
    }

    private StandardItem getEntityById(UUID itemId) {
        return standardItemRepository.findById(itemId)
                .orElseThrow(StandardItemNotFoundException::new);
    }
}