package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImplementation implements ProductService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;


    private Product dtoToEntity(ProductDTO productDTO){
        Product product = new Product();

        product.setProductName(productDTO.getProductName());
        product.setDescription(productDTO.getDescription());
        product.setQuantity(productDTO.getQuantity());
        product.setPrice(productDTO.getPrice());
        product.setDiscount(productDTO.getDiscount());

        return product;
    }

    private ProductDTO entityToDTO(Product product){
        ProductDTO productDTO = new ProductDTO();

        productDTO.setProductId(product.getProductId());
        productDTO.setProductName(product.getProductName());
        productDTO.setDescription(product.getDescription());
        productDTO.setImage(product.getImage());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setPrice(product.getPrice());
        productDTO.setDiscount(product.getDiscount());
        productDTO.setSpecialPrice(product.getSpecialPrice());

        return productDTO;
    }

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Product product = dtoToEntity(productDTO);

        List<Product> productList = category.getProductList();

        for(Product _product: productList){
            if(_product.getProductName().equalsIgnoreCase(product.getProductName())){
                throw new APIException("Product '"+product.getProductName()+"' already exist!");
            }
        }

        product.setCategory(category);

        String image = "default.png";
        product.setImage(image);
        productDTO.setImage(image);

        Double specialPrice = product.getPrice() * (100-product.getDiscount())/100;
        product.setSpecialPrice(specialPrice);
        productDTO.setSpecialPrice(specialPrice);

        // save the product
        Product savedProduct = productRepository.save(product);
        return entityToDTO(savedProduct);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findAll(pageDetails);
        List<Product> productList = productPage.getContent();

        if(productList.isEmpty()){
            throw new APIException("Products does not exist yet");
        }

        List<ProductDTO> productDTOS = productList.stream().map(this::entityToDTO).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setLastPage(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryID", categoryId));

        Sort sortByAndOrder = (sortBy.equalsIgnoreCase("asc"))
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> productPage = productRepository.findByCategoryOrderByPriceAsc(category, pageDetails);

        List<Product> productsList = productPage.getContent();

        if(productsList.isEmpty()){
            throw new APIException("Products does not exist yet in the Category ID: '"+categoryId+"'");
        }

        List<ProductDTO> productDTOList = productsList.stream().map(this::entityToDTO).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setLastPage(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = (sortBy.equalsIgnoreCase("asc"))
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase("%"+keyword+"%", pageDetails);

        List<Product> productsList = productPage.getContent();

        if(productsList.isEmpty()){
            throw new APIException("Products does not exist with the keyword: '"+keyword+"'");
        }

        List<ProductDTO> productDTOList = productsList.stream().map(this::entityToDTO).toList();

        ProductResponse productResponse = new ProductResponse();

        productResponse.setContent(productDTOList);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setLastPage(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        product.setDescription(productDTO.getDescription());
        product.setProductName(productDTO.getProductName());
        product.setQuantity(productDTO.getQuantity());
        product.setPrice(productDTO.getPrice());
        product.setDiscount(productDTO.getDiscount());

        Double specialPrice = product.getPrice() * (100-product.getDiscount())/100;
        product.setSpecialPrice(specialPrice);
        productDTO.setSpecialPrice(specialPrice);

        Product savedProduct = productRepository.save(product);
        return entityToDTO(savedProduct);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        productRepository.delete(product);
        return entityToDTO(product);
    }

    @Override
    public ProductDTO uploadProductImage(Long productId, MultipartFile image) throws IOException {
        // get product from db
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // upload the image to server
        // get the file name of the uploaded image
        String fileName = fileService.uploadImage(path, image);

        // update the new file name oto the product
        product.setImage(fileName);
        productRepository.save(product);

        return entityToDTO(product);
    }



}
