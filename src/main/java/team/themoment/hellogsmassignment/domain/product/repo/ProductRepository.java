package team.themoment.hellogsmassignment.domain.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import team.themoment.hellogsmassignment.domain.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
