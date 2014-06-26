-- 
-- ***** BEGIN LICENSE BLOCK *****
-- Zimbra Collaboration Suite Server
-- Copyright (C) 2012, 2013, 2014 Zimbra, Inc.
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
create table actions ( 
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
 name VARCHAR(256) 
 );
insert into actions (name) VALUES ('ZmMailApp');
insert into actions (name) VALUES ('ZmMailItem');
insert into actions (name) VALUES ('ZmCalendarApp');
insert into actions (name) VALUES ('ZmContactsApp');
insert into actions (name) VALUES ('ZmContactsItem');
insert into actions (name) VALUES ('ZmTasksApp');
insert into actions (name) VALUES ('ZmTaskItem');

