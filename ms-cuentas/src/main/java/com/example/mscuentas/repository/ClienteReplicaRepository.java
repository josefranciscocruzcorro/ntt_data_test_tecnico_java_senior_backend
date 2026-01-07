package com.example.mscuentas.repository;

import com.example.mscuentas.entity.ClienteReplica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteReplicaRepository extends JpaRepository<ClienteReplica, Long> {
}
