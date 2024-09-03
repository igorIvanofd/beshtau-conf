package com.terra.beshtau.conf.services;

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.terra.beshtau.conf.data.BeshtauRepository;
import com.terra.beshtau.conf.dto.ForeignData;
import com.terra.beshtau.conf.mapper.BeshtauMapper;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class TableFileReader {
    private final BeshtauRepository repository;
    private final BeshtauMapper mapper;

    public TableFileReader(BeshtauRepository repository,
                           BeshtauMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public void invoke(InputStream is) {
        List<ForeignData> data = Poiji.fromExcel(is, PoijiExcelType.XLSX, ForeignData.class);
        repository.saveAll(data.stream().map(mapper::mapToEntity).toList());
        repository.flush();
    }
}
