create TABLE t_user_normal (
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  tenant_id BIGINT(20) NOT NULL,
  username VARCHAR(128) NOT NULL,
  gender tinyint NOT NULL default 1,
  industry VARCHAR(64) NOT NULL DEFAULT '',
  address VARCHAR(256) NOT NULL DEFAULT '',
  country VARCHAR(256) NOT NULL DEFAULT '',
  province VARCHAR(256) NOT NULL DEFAULT '',
  city VARCHAR(256) NOT NULL DEFAULT '',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON update CURRENT_TIMESTAMP(3),
  PRIMARY KEY (id),
  UNIQUE KEY uk_username (tenant_id,username),
  INDEX idx_updated_at (updated_at)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

create TABLE t_user_flag_date (
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  tenant_id BIGINT(20) NOT NULL,
  username VARCHAR(128) NOT NULL,
  gender tinyint NOT NULL default 1,
  industry VARCHAR(64) NOT NULL DEFAULT '',
  address VARCHAR(256) NOT NULL DEFAULT '',
  country VARCHAR(256) NOT NULL DEFAULT '',
  province VARCHAR(256) NOT NULL DEFAULT '',
  city VARCHAR(256) NOT NULL DEFAULT '',
  deleted_at DATETIME(3) NOT NULL,
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON update CURRENT_TIMESTAMP(3),
  PRIMARY KEY (id),
  UNIQUE KEY uk_username_flag (tenant_id,username,deleted_at),
  INDEX idx_updated_at (updated_at)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

create TABLE t_user_flag_long (
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  tenant_id BIGINT(20) NOT NULL,
  username VARCHAR(128) NOT NULL,
  gender tinyint NOT NULL default 1,
  industry VARCHAR(64) NOT NULL DEFAULT '',
  address VARCHAR(256) NOT NULL DEFAULT '',
  country VARCHAR(256) NOT NULL DEFAULT '',
  province VARCHAR(256) NOT NULL DEFAULT '',
  city VARCHAR(256) NOT NULL DEFAULT '',
  deleted_at BIGINT(20) NOT NULL DEFAULT 0,
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON update CURRENT_TIMESTAMP(3),
  PRIMARY KEY (id),
  UNIQUE KEY uk_username_flag (tenant_id,username,deleted_at),
  INDEX idx_updated_at (updated_at)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;