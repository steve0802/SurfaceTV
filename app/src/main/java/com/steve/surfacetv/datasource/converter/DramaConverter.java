package com.steve.surfacetv.datasource.converter;

import com.steve.surfacetv.datasource.api.ro.DramaRo;
import com.steve.surfacetv.datasource.db.entity.DramaPo;
import com.steve.surfacetv.datasource.vo.DramaVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DramaConverter {
    private static DramaConverter instance;

    SimpleDateFormat simpleDateFormatForParse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    SimpleDateFormat simpleDateFormatForOutput = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US);

    public static DramaConverter getInstance() {
        if (instance == null)
            instance = new DramaConverter();
        return instance;
    }

    private DramaConverter() {
    }

    public DramaPo convert(DramaRo dramaRO) throws ParseException {
        String createdAt = "";
        Date createdAtDate = simpleDateFormatForParse.parse(dramaRO.getCreated_at());
        if (createdAtDate != null)
            createdAt = simpleDateFormatForOutput.format(createdAtDate);

        double rating = BigDecimal.valueOf(dramaRO.getRating()).setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();

        return new DramaPo(
                dramaRO.getDrama_id(),
                dramaRO.getName(),
                dramaRO.getTotal_views(),
                createdAt,
                dramaRO.getThumb(),
                rating
            );
    }

    public DramaVo convert(DramaPo dramaPo) {
        DramaVo dramaVo = new DramaVo();
        dramaVo.setDramaId(dramaPo.getDrama_id());
        dramaVo.setName(dramaPo.getName());
        dramaVo.setTotalViews(dramaPo.getTotal_views());
        dramaVo.setCreatedAt(dramaPo.getCreated_at());
        dramaVo.setThumb(dramaPo.getThumb());
        dramaVo.setRating(dramaPo.getRating());
        return dramaVo;
    }
}
