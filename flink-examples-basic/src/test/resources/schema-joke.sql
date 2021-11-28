CREATE TABLE joke(
     sid VARCHAR(32) NOT NULL COMMENT '段子ID',
     text VARCHAR(1024) NOT NULL COMMENT '段子标题',
     type VARCHAR(16) NOT NULL COMMENT '段子分类:video|image|gif|text',
     thumbnail VARCHAR(256) COMMENT '缩略图URL',
     images VARCHAR(256) COMMENT '图片URL,当type=image/gif时有值',
     video VARCHAR(256) COMMENT '视频URL,当type=video时有值',
     up INT DEFAULT 0 COMMENT 'up数量',
     down INT DEFAULT 0 COMMENT 'down数量',
     forward INT DEFAULT 0 COMMENT 'forward数量',
     comment INT DEFAULT 0 COMMENT 'comment数量',
     passtime DATETIME NOT NULL COMMENT '发布时间',
     PRIMARY KEY (sid)
)