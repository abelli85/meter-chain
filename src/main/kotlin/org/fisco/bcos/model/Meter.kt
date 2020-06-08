package org.fisco.bcos.model

import com.alibaba.fastjson.annotation.JSONField
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
@CompoundIndexes(
        CompoundIndex(def = "{'meterId': 1, 'batchId': 1}", unique = true)
)
class Meter {
    /**
     * 委托单号， 按年+月+4位顺序号: 2020061234.
     */
    @Indexed
    var batchId: String? = null

    /**
     *
     */
    @Indexed
    var meterId: String? = null

    /**
     * 检定时间，ISO8601格式，如：2020-06-01T16:01:02Z (GMT时间，北京时间 2020-6-1 8:01:02)
     * 或包含时区: 2020-06-01T08:01:02+0800 (后面不再+Z)
     */
    @Transient
    var verifyTime: String? = null

    /**
     * 检定结果， PASS - 合格; FAIL - 不合格.
     */
    @Transient
    var result: String? = null

    /**
     * 检定有效期.
     */
    @Transient
    @JSONField(format = MeterBatch.ISO_DATE_TIME_FMT)
    var validDate: Date? = null

    /**
     * 检定员姓名
     */
    @Transient
    var verifierName: String? = null

    companion object {
        const val KEY_METER_ID = "meterId"
    }
}