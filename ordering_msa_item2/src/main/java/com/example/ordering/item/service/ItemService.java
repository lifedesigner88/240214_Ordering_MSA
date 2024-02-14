package com.example.ordering.item.service;

import com.example.ordering.item.domain.Item;
import com.example.ordering.item.dto.ItemReqDto;
import com.example.ordering.item.dto.ItemResDto;
import com.example.ordering.item.dto.ItemSearchDto;
import com.example.ordering.item.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemService {

    private final ItemRepository itemRepo;

    public ItemService(@Autowired ItemRepository itemRepo) {
        this.itemRepo = itemRepo;
    }


    //    Create
    public Item createItem(ItemReqDto itemReqDto) {
        Item itemTemp = new Item(itemReqDto);
        MultipartFile multipartFile = itemReqDto.getItemImage();
        savefile(itemTemp, multipartFile);
        return itemRepo.save(itemTemp);
    }


    public List<ItemResDto> SerchItems(ItemSearchDto searchDto, Pageable pageable) {
        // 검색을 위해 Specification 객체를 사용
        // Specification 객체는 복잡한 쿼리를 명세를 이용한 정의를 하여 쉽게 생성
        Specification<Item> spec = new Specification<Item>() {

            @Override
            // root: 엔티티의 속성에 접근하기 위한 객체
            // CriteriaBuilder: 쿼리를 생성하기 위한 객체
            public Predicate toPredicate(Root<Item> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder
                        .equal(root.get("delYn"), "N"));
                if (searchDto.getName() != null)
                    predicates.add(criteriaBuilder
                            .like(root.get("name"), "%" + searchDto.getName() + "%"));
                if (searchDto.getCategory() != null)
                    predicates.add(criteriaBuilder
                            .equal(root.get("category"), searchDto.getCategory()));

                Predicate[] predicateArr = new Predicate[predicates.size()];
                for (int i = 0; i < predicates.size(); i++)
                    predicateArr[i] = predicates.get(i);
                return criteriaBuilder.and(predicateArr);
            }
        };
        Page<Item> items = itemRepo.findAll(spec, pageable);
        return items.stream()
                .map(ItemResDto::new)
                .collect(Collectors.toList());
    }


    //    Read
    public Resource getImage(Long id) {
        Item item = itemRepo.findById(id).orElseThrow(EntityNotFoundException::new);
        String imagepath = item.getImagePath();
        Path path = Paths.get(imagepath);
        try {
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("url form is not valid");
        }
    }


    //    Update
    public Item updateItem(Long id, ItemReqDto itemReqDto) {
        Item itemTemp = itemRepo.findById(id).orElseThrow(EntityNotFoundException::new);
        MultipartFile multipartFile = itemReqDto.getItemImage();
        itemTemp.update(itemReqDto);
        savefile(itemTemp, multipartFile);
        return itemRepo.save(itemTemp);
    }

    //    Delete
    public Object delete(Long id) {
        Item item = itemRepo.findById(id).orElseThrow();
        item.delete();
        return item;
    }


    //    duplicated funtion
    private void savefile(Item itemTemp, MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        Long itemId = itemRepo.save(itemTemp).getId();
        Path path = Paths
                .get(
                        "C:\\Users\\Playdata\\IdeaProjects\\Ordering\\src\\main\\resources\\temp",
//                        "C:\\Users\\LifeD\\IdeaProjects\\SpringBoot_Book\\240131_Ordering_Spring\\src\\main\\resources\\temp",
                        itemId + "_" + fileName);
        itemTemp.setImagePath(path.toString());
        try {
            byte[] bytes = multipartFile.getBytes();
            Files.write(path, bytes,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new IllegalArgumentException("image not available");
        }
    }
}
