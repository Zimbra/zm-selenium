-- 
-- ***** BEGIN LICENSE BLOCK *****
-- Zimbra Collaboration Suite Server
-- Copyright (C) 2012, 2013 Zimbra, Inc.
-- 
-- This program is free software: you can redistribute it and/or modify it under
-- the terms of the GNU General Public License as published by the Free Software Foundation,
-- version 2 of the License.
-- 
-- This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
-- without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
-- See the GNU General Public License for more details.
-- You should have received a copy of the GNU General Public License along with this program.
-- If not, see <http://www.gnu.org/licenses/>.
-- ***** END LICENSE BLOCK *****
-- 
create table apps ( 
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
 name VARCHAR(256) 
 );
insert into apps (name) VALUES ('AJAX');
insert into apps (name) VALUES ('HTML');
insert into apps (name) VALUES ('MOBILE');
insert into apps (name) VALUES ('ADMIN');
insert into apps (name) VALUES ('DESKTOP');
insert into apps (name) VALUES ('OCTOPUS');