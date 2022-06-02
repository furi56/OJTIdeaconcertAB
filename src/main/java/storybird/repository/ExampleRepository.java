package storybird.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import storybird.entity.MemberEntity;
import storybird.vo.MemberVo;

import java.util.List;

public interface ExampleRepository extends JpaRepository<MemberEntity, Long> {
    List<MemberEntity> findAll();
}
