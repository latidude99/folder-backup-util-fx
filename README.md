# Folder Backup Util FX

[![](https://img.shields.io/badge/release-0.0-blue.svg)](https://github.com/latidude99/enquiries/tree/master/release)
[![License: GPL v3](https://img.shields.io/badge/license-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![GitHub issues](https://img.shields.io/badge/issues-open%200-greenred.svg)](https://GitHub.com/latidude99/enquiries/issues/)
[![](https://img.shields.io/badge/%20$%20-buy%20me%20a%20coffe-yellow.svg)](https://www.buymeacoffee.com/zWn1I6bVf)

A command-line tool for compressing folders to ZIP format.


# General Info

FolderBackupUtilFX packs the content of the folder it is executed from
(files, folders and subfolders) into a ZIP file. You can exclude
 files and folders listing them in the app.config file (which 
if`exist is read at the application start). The app.config file contains explanations 
how to use it.
  
  
The content of the result  ZIP file may include the original FolderBackupToolFX_vXX.jar file (defined in app.config)   
    

By default the ZIP file name is composed of: `folderName` + `_backup_` + `dateandtime` + `.zip` 
(eg. `FolderBackupUtilFX_backup_2019-07-01 10.51.zip`) unless configured otherwise in app.config.

# Status

In progress, not fully working yet.

# Technologies
- Java JDK 12
- JavaFX 12
- Maven 3.60
- IntelliJ IDEA. 

**Plugins and libraries**
- Apache Shade Maven plugin  version 2.4.1


# License
FolderBackupUtilFX is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License 
as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
FolderBackupUtilFX is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 
warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with FolderBackupUtilFX. 
If not, see http://www.gnu.org/licenses/ or write to: latidude99@gmail.com

# Contact
You can email me at latidude99@gmail.com

