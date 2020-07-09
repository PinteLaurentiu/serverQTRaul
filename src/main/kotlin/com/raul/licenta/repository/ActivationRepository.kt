package com.raul.licenta.repository

import com.raul.licenta.model.Activation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ActivationRepository : JpaRepository<Activation, Long>
