package com.ruoyi.common.utils.snowflake;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;

/**
 * @author: dyw
 * @date: 2023/5/25 14:29
 */
public class SnowFlakeUtil {

    /**
     * yitter工具类配置
     */
    private static boolean isOptions() {
        boolean flag = false;
        if (YitIdHelper.getIdGenInstance() != null) {
            flag = true;
        }
        return flag;
    }

    /**
     * yitter工具类生产的雪花算法id。生成雪花主键id。
     * @return 雪花型id
     */
    public static String snowFlakeId() {
        if (!isOptions()) {
            IdGeneratorOptions options = new IdGeneratorOptions((short) 20);
            options.WorkerIdBitLength = 16;
            YitIdHelper.setIdGenerator(options);
        }
        long newId = YitIdHelper.nextId();
        return newId + "";
    }
}
