package org.fisco.bcos.model

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.annotation.JSONField
import org.joda.time.format.ISODateTimeFormat
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable
import java.util.*

/**
 * 水表在安装到居民住宅前，按国家计量法须检定合格, 且必须在有效期内使用。
 * 水表按委托单进行批量检定，每个委托单包含1～100只水表。
 *
 * 检定内容包括: 外观检定、压力检定、机电转换检定、流量点检定，其中任何一项不合格，该只水表均被认定为不合格。
 * 在检定内容中，以流量点检定过程最为复杂，以DN15水表为例， 需检定3个常用流量点，分别是:
 * Q1 - 31.25 L/h, 误差要求 +/-5.0%
 * Q2 - 50 L/h, 误差要求 +/-2.0%
 * Q3 - 2500 L/h, 误差要求 +/-2.0%
 * 在自动化检定台上，控制水泵保持恒定流量，通过视觉识别水表字轮，分别记录在Q1/Q2/Q3恒定流量下的起至读数，
 * 并与检定台水泵流量计的计量水量进行比较，计算每个流量点的误差值。
 * 任何一个流量点的误差值超过规程要求，均被认定为不合格.

 * 注：
 * - 从常用水表口径 DN15、DN20、DN25、...、DN150、DN200、DN300 等居民、工商业用户的水表，其需要
 * 检定的流量点的数值和个数都是不相同的，口径越大的水表，需要检定的流量点个数越多。
 * - DN40口径以下水表的检定有效期一般为6年，以上口径的检定有效期为2年.
 */
@Document
class MeterBatch {
    /**
     * 委托单号， 按年+月+4位顺序号: 2020061234.
     */
    @Id
    var batchId: String? = null

    /**
     * 水表厂家.
     */
    @Indexed
    var manufacturer: String? = null

    /**
     * 表码清单， 最大16位，不足16位后补零.
     */
    var meterList: List<String>? = null

    @Transient
    var meterCount: Int? = null
        get() = meterList?.size

    /**
     * 送达日期， 可以开始检定.
     */
    @JSONField(format = ISO_DATE_TIME_FMT)
    var deliverDate: Date? = null

    /**
     * 完成日期.
     */
    @JSONField(format = ISO_DATE_TIME_FMT)
    var finishDate: Date? = null

    /**
     * 检定有效期.
     */
    @JSONField(format = ISO_DATE_TIME_FMT)
    var validDate: Date? = null

    /**
     * 检定员姓名
     */
    @Indexed
    var verifierName: String? = null

    /**
     * 检定员地址
     */
    var verifierAddress: String? = null

    /**
     * 合约地址
     */
    var contractAddress: String? = null

    companion object {
        const val ISO_DATE_TIME_FMT = "yyyy-MM-dd'T'HH:mm:ssZ"
    }
}