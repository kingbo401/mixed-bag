'CREATE TABLE `sequence` (
  `id` int(11) NOT NULL COMMENT ''主键'',
  `name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT ''序列名，一般为表名'',
  `value` bigint(20) DEFAULT NULL COMMENT ''序列最新值'',
  `idx` int(11) DEFAULT ''0'' COMMENT ''第几张表，默认0'',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT=''序列生成器，支持分表；分表时许把每张表初始化到，字段idx代表第几张表'''