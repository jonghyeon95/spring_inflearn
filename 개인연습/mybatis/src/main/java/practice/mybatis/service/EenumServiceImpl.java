package practice.mybatis.service;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.mybatis.domain.Eenum;
import practice.mybatis.domain.EenumDto;
import practice.mybatis.domain.MemberDto;
import practice.mybatis.mapper.EenumMapper;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class EenumServiceImpl {

    private final EenumMapper eenumMapper;

    int AddEenum(EenumDto eenum){
        return eenumMapper.addEenum(eenum);
    }

    List<EenumDto> selectEenum(){
        return eenumMapper.getEenumList();
    }

    List<Map> selectJoin(){
        return eenumMapper.selectJoin();
    }

    List<Map> search(EenumDto eenumDto, MemberDto memberDto){
        return eenumMapper.search(eenumDto, memberDto);
    }

}
