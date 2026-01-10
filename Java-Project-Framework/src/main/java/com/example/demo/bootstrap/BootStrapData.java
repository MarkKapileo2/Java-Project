package com.example.demo.bootstrap;

import com.example.demo.domain.OutsourcedPart;
import com.example.demo.domain.Part;
import com.example.demo.domain.Product;
import com.example.demo.repositories.OutsourcedPartRepository;
import com.example.demo.repositories.PartRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.service.OutsourcedPartService;
import com.example.demo.service.OutsourcedPartServiceImpl;
import com.example.demo.service.ProductService;
import com.example.demo.service.ProductServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.*;

/**
 *
 *
 *
 *
 */
@Component
public class BootStrapData implements CommandLineRunner {

    private final PartRepository partRepository;
    private final ProductRepository productRepository;

    private final OutsourcedPartRepository outsourcedPartRepository;

    public BootStrapData(PartRepository partRepository, ProductRepository productRepository, OutsourcedPartRepository outsourcedPartRepository) {
        this.partRepository = partRepository;
        this.productRepository = productRepository;
        this.outsourcedPartRepository=outsourcedPartRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (partRepository.count() == 0 && productRepository.count() == 0) {
            Set<String> partNames = new HashSet<>();
            List<OutsourcedPart> parts = new ArrayList<>();

            String[][] sampleParts = {
                    {"Intel i7 CPU", "10"},
                    {"16GB RAM", "20"},
                    {"512GB SSD", "15"},
                    {"NVIDIA RTX 3060", "8"},
                    {"650W Power Supply", "12"},
                    {"16GB RAM", "5"},
                    {"650W Power Supply", "3"}
            };

            for (String[] data : sampleParts) {
                String name = data[0];
                int inventory = Integer.parseInt(data[1]);

                if (!partNames.add(name)) {
                    name += " - Multi-Pack";
                }

                OutsourcedPart part = new OutsourcedPart();
                part.setName(name);
                part.setInv(inventory);
                part.setPrice(50.0);
                part.setCompanyName("Generic Supplier");
                part.setMin(1);
                part.setMax(25);
                parts.add(part);
            }

            outsourcedPartRepository.saveAll(parts);

            List<Product> products = List.of(
                    new Product("Gaming PC", 100.0, 5),
                    new Product("Office PC", 80.0, 6),
                    new Product("Streaming PC", 120.0, 3),
                    new Product("Mini PC", 70.0, 4),
                    new Product("Workstation", 150.0, 2)
            );

            productRepository.saveAll(products);
        }

        List<OutsourcedPart> outsourcedParts=(List<OutsourcedPart>) outsourcedPartRepository.findAll();
        for(OutsourcedPart part:outsourcedParts){
            System.out.println(part.getName()+" "+part.getCompanyName());
        }

        System.out.println("Started in Bootstrap");
        System.out.println("Number of Products"+productRepository.count());
        System.out.println(productRepository.findAll());
        System.out.println("Number of Parts"+partRepository.count());
        System.out.println(partRepository.findAll());

    }
}
