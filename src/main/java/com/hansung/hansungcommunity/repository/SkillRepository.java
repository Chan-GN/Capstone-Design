package com.hansung.hansungcommunity.repository;

import com.hansung.hansungcommunity.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findByNameIn(List<String> skills);

}
