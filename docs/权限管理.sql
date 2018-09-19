SET FOREIGN_KEY_CHECKS=0

DROP TABLE IF EXISTS `Group` CASCADE
;

DROP TABLE IF EXISTS `OperationLog` CASCADE
;

DROP TABLE IF EXISTS `Permission` CASCADE
;

DROP TABLE IF EXISTS `Product` CASCADE
;

DROP TABLE IF EXISTS `Role` CASCADE
;

DROP TABLE IF EXISTS `Tag` CASCADE
;

DROP TABLE IF EXISTS `User` CASCADE
;

DROP TABLE IF EXISTS `UserGroup` CASCADE
;

DROP TABLE IF EXISTS `UserRole` CASCADE
;

CREATE TABLE `Group`
(
	`GroupID` INTEGER NOT NULL,
	`GroupName` VARCHAR(50) NOT NULL,
	`ParentID` VARCHAR(50) NOT NULL,
	`CreateTime` DATETIME(0) NOT NULL,
	`Comment` VARCHAR(200),
	CONSTRAINT `PK_Group` PRIMARY KEY (`GroupID`)
)
;

CREATE TABLE `OperationLog`
(
	`OperationLogID` INTEGER NOT NULL,
	`OperationType` INT NOT NULL,
	`Content` VARCHAR(200) NOT NULL,
	`UserID` INTEGER NOT NULL,
	`OperationTime` DATETIME(0) NOT NULL,
	CONSTRAINT `PK_OperationLog` PRIMARY KEY (`OperationLogID`)
)
;

CREATE TABLE `Permission`
(
	`PermissionID` INTEGER NOT NULL,
	`PermissionName` VARCHAR(50) NOT NULL,
	`Type` VARCHAR(50),
	`SourceType` VARCHAR(50),
	`SourceID` VARCHAR(50),
	`Resource` VARCHAR(50),
	`ResourceID` VARCHAR(50),
	`Tags` VARCHAR(50),
	`Chmod` VARCHAR(50),
	CONSTRAINT `PK_Permission` PRIMARY KEY (`PermissionID`)
)
;

CREATE TABLE `Product`
(
	`ProductID` INTEGER NOT NULL,
	`ProductName` VARCHAR(50) NOT NULL,
	`Sort` INT,
	`Code` NVARCHAR(50) NOT NULL,
	`Icon` NVARCHAR(100),
	`Ower` INT,
	`CreateTime` DATETIME(0) NOT NULL,
	CONSTRAINT `PK_Application` PRIMARY KEY (`ProductID`)
)
;

CREATE TABLE `Role`
(
	`RoleID` INTEGER NOT NULL,
	`RoleName` NVARCHAR(50) NOT NULL,
	`State` INTEGER,
	`CreateTime` TIME(0) NOT NULL,
	`Comment` NVARCHAR(500),
	`ProductID` INTEGER,
	CONSTRAINT `PK_Role` PRIMARY KEY (`RoleID`)
)
;

CREATE TABLE `Tag`
(
	`ID` INTEGER NOT NULL,
	`TagName` VARCHAR(50),
	`Comment` VARCHAR(200)
)
;

CREATE TABLE `User`
(
	`UserID` INTEGER NOT NULL,
	`UserName` NVARCHAR(100),
	`TrueName` NVARCHAR(100),
	`Password` NVARCHAR(50),
	`Telphone` NVARCHAR(50),
	`Email` NVARCHAR(50),
	`Mobile` INT,
	`WeiChat` VARCHAR(50),
	`QQ` INTEGER,
	`Sex` INT,
	`Address` NVARCHAR(500),
	`State` INT,
	`CreateTime` TIME(0),
	`Comment` NVARCHAR(500),
	CONSTRAINT `PK_User` PRIMARY KEY (`UserID`)
)
;

CREATE TABLE `UserGroup`
(
	`UserGroupID` INTEGER NOT NULL,
	`UserID` INTEGER NOT NULL,
	`GroupID` INTEGER NOT NULL,
	CONSTRAINT `PK_UserGroupRelation` PRIMARY KEY (`UserGroupID`)
)
;

CREATE TABLE `UserRole`
(
	`UserRoleID` INTEGER NOT NULL,
	`UserID` INTEGER NOT NULL,
	`RoleID` INTEGER NOT NULL,
	CONSTRAINT `PK_UserRoleRelation` PRIMARY KEY (`UserRoleID`)
)
;

ALTER TABLE `OperationLog` 
 ADD INDEX `IXFK_OperationLog_User` (`UserID` ASC)
;

ALTER TABLE `Role` 
 ADD INDEX `IXFK_Role_Application` (`ProductID` ASC)
;

ALTER TABLE `UserGroup` 
 ADD INDEX `IXFK_UserGroupRelation_Group` (`GroupID` ASC)
;

ALTER TABLE `UserGroup` 
 ADD INDEX `IXFK_UserGroupRelation_User` (`UserID` ASC)
;

ALTER TABLE `UserRole` 
 ADD INDEX `IXFK_UserRoleRelation_Role` (`RoleID` ASC)
;

ALTER TABLE `UserRole` 
 ADD INDEX `IXFK_UserRoleRelation_User` (`UserID` ASC)
;

ALTER TABLE `Role` 
 ADD CONSTRAINT `FK_Role_Application`
	FOREIGN KEY (`ProductID`) REFERENCES `Product` (`ProductID`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `UserGroup` 
 ADD CONSTRAINT `FK_UserGroupRelation_Group`
	FOREIGN KEY (`GroupID`) REFERENCES `Group` (`GroupID`) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE `UserGroup` 
 ADD CONSTRAINT `FK_UserGroupRelation_User`
	FOREIGN KEY (`UserID`) REFERENCES `User` (`UserID`) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE `UserRole` 
 ADD CONSTRAINT `FK_UserRoleRelation_Role`
	FOREIGN KEY (`RoleID`) REFERENCES `Role` (`RoleID`) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE `UserRole` 
 ADD CONSTRAINT `FK_UserRoleRelation_User`
	FOREIGN KEY (`UserID`) REFERENCES `User` (`UserID`) ON DELETE No Action ON UPDATE No Action
;

SET FOREIGN_KEY_CHECKS=1
