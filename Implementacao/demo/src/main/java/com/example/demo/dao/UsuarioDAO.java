package com.example.demo.dao;

import java.util.*;
import com.example.demo.model.Usuario;

public interface UsuarioDAO {

   Usuario save(Usuario usuario);
   Optional<Usuario> findById(Long id);
   Optional<Usuario> findByEmail(String email);
   List<Usuario> findAll();
   void delete(Usuario usuario);
   
}
