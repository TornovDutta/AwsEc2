package com.example.AwsEc2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserREpo extends JpaRepository<Users,Integer> {
}
