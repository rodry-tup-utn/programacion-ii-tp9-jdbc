CREATE TABLE `categoria` (
                             `id` int(11) NOT NULL AUTO_INCREMENT,
                             `nombre` varchar(100) NOT NULL,
                             `descripcion` varchar(255) DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

CREATE TABLE `items_pedido` (
                                `id_items_pedido` int(11) NOT NULL AUTO_INCREMENT,
                                `id_pedido` int(11) NOT NULL,
                                `id_producto` int(11) NOT NULL,
                                `cantidad` int(11) NOT NULL,
                                `subtotal` double NOT NULL,
                                PRIMARY KEY (`id_items_pedido`),
                                KEY `id_pedido` (`id_pedido`),
                                KEY `id_producto` (`id_producto`),
                                CONSTRAINT `items_pedido_ibfk_1` FOREIGN KEY (`id_pedido`) REFERENCES `pedido` (`id_pedido`),
                                CONSTRAINT `items_pedido_ibfk_2` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

CREATE TABLE `pedido` (
                          `id_pedido` int(11) NOT NULL AUTO_INCREMENT,
                          `fecha` date DEFAULT NULL,
                          `total` double DEFAULT NULL,
                          PRIMARY KEY (`id_pedido`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

CREATE TABLE `producto` (
                            `id_producto` int(11) NOT NULL AUTO_INCREMENT,
                            `nombre` varchar(100) DEFAULT NULL,
                            `descripcion` varchar(255) DEFAULT NULL,
                            `precio` double DEFAULT NULL,
                            `cantidad` int(11) DEFAULT NULL,
                            `id_categoria` int(11) DEFAULT NULL,
                            PRIMARY KEY (`id_producto`),
                            KEY `id_categoria` (`id_categoria`),
                            CONSTRAINT `producto_ibfk_1` FOREIGN KEY (`id_categoria`) REFERENCES `categoria` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;