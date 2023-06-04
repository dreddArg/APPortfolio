package com.portfolio.BackEnd.Repository;

import com.portfolio.BackEnd.Entity.Habilidades;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHabilidadesRepository extends JpaRepository<Habilidades, Integer> {
    
    public Optional<Habilidades> findByNombreHabilidades(String nombreHabilidades);
    
    public boolean existsByNombreHabilidades(String nombreHabilidades);
}
