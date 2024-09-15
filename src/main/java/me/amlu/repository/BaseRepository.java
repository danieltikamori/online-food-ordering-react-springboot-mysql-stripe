package me.amlu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface BaseRepository<T, I extends Serializable> extends JpaRepository<T, I> {

    List<T> findAllByDeletedAtIsNull();
}