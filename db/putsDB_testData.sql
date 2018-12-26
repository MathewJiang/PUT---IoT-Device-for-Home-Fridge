INSERT INTO `content_units` VALUES ('ml'), ('g'), ('lb'), ('items'), ('default');
INSERT INTO `package_units` VALUES ('bag'), ('bottle'), ('box'), ('unpackaged'), ('default');
INSERT INTO `vendors` VALUES ('Metro'), ('Loblaws'), ('Sobbeys'), ('Veda'), ('McDonalds'), ('Lucky Moose');
INSERT INTO `brands` VALUES ('Dole'), ('Natrel'), ('Sealtest'), ('Lucky Moose'), ('Prestige'), ('Fanny Bay');
INSERT INTO `actions` VALUES ('consumption'), ('disposal');
INSERT INTO `categories` VALUES ('drink'), ('fruit'), ('dairy'), ('egg'), ('seafood'), ('meat');
INSERT INTO `statuses` VALUES ('good'), ('finished'), ('disposed'), ('expired');
INSERT INTO `std_names` VALUES ('3.25% milk', 'dairy'),
			('whole chicken', 'meat'),
			('10% cream', 'dairy'),
			('egg', 'egg'),
			('oyster meat', 'seafood');

INSERT INTO `barcodes` VALUES ('064420000224', 'Sealtest', '3.25% milk', 1000, 'ml', 'T', 'box'),
			('055872102895', 'Natrel', '10% cream', 500, 'ml', 'T', 'box'),
			('053582317029', 'Lucky moose', 'whole chicken', 2, 'lb', 'T', 'bag'),
			('073729462836', 'Fanny Bay', 'oyster meat', 500, 'g', 'T', 'box'),
			('064767342001', 'Prestige', 'egg', 12, 'items', 'T', 'box');

INSERT INTO `purchase_history` (std_name, vendor, brand, content_quantity, content_unit, is_packaged, package_unit, purchase_date, expiry_date) VALUES 
			('egg', 'Metro', 'Prestige', '12', 'items', 'T', 'box', '2018-12-21', '2018-12-31'),
			('egg', 'Metro', 'Prestige', '12', 'items', 'T', 'box', '2018-12-21', '2018-12-28'),
			('3.25% milk', 'Metro', 'Sealtest', '1000', 'ml', 'T', 'box', '2018-12-10', '2018-12-20'),
			('3.25% milk', 'Metro', 'Prestige', '1000', 'ml', 'T', 'box', '2018-12-21', '2018-12-31'),
			('oyster meat', 'Metro', 'Fanny Bay', '500', 'g', 'T', 'box', '2018-12-26', '2018-12-28'),
			('whole chicken', 'Lucky Moose', 'Lucky Moose', '3', 'lb', 'T', 'box', '2018-12-21', '2018-12-31');

INSERT INTO `consumption_history` (uid, std_name, consumed_quantity, remaining_quantity, action, time_stamp) VALUES
			(1, 'egg', 6, 6, 'consumption', '2018-12-24 09:16:32'),
			(1, 'egg', 6, 6, 'consumption', '2018-12-25 09:16:32'),
			(2, 'egg', 12, 0, 'disposal', '2018-12-26 09:16:32'),
			(4, '3.25% milk', 500, 500, 'consumption', '2018-12-24 09:16:32'),
			(4, '3.25% milk', 500, 0, 'consumption', '2018-12-25 09:16:32'),
			(5, 'oyster meat', 500, 250, 'consumption', '2018-12-26 09:16:32'),
			(6, 'whole chicken', 2, 1, 'consumption', '2018-12-27 09:16:32');

INSERT INTO `void_items` VALUES
			(1, 'egg', 'finished', '2018-12-25 09:16:32'),
			(4, '3.25% milk', 'finished', '2018-12-25 09:16:32'),
			(2, '3.25% milk', 'disposed', '2018-12-26 09:16:32');

INSERT INTO `grocery_storage` VALUES
			(3, '3.25% milk', 1000, 'ml', '2018-12-10 06:16:32', '2018-12-10', '2018-12-20', 'expired'),
			(5, 'oyster meat', 250, 'g', '2018-12-26 09:16:32', '2018-12-26', '2018-12-28', 'good'),
			(6, 'whole chicken', 1, 'lb', '2018-12-27 09:16:32', '2018-12-21', '2018-12-31', 'good');

INSERT INTO `temperature_data` (time, temperature) VALUES
			('2018-12-26 03:04:05', -3.2),
			('2018-12-26 05:04:05', -4.2),
			('2018-12-26 07:04:05', -5.2),
			('2018-12-26 09:04:05', -2.2),
			('2018-12-26 11:04:05', -3.2);


			


