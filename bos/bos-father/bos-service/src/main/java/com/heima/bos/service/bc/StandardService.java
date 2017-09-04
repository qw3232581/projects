package com.heima.bos.service.bc;

import com.heima.bos.domain.bc.Standard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StandardService {

    void save(Standard model);

    Page<Standard> pageQuery(Pageable pageable);

    void delStandard(long parseLong);

    void restoreStandard(long parseLong);

    List<Standard> findAllInUse();


}
