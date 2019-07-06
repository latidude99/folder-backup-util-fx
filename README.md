# Folder Backup Util FX

[![](https://img.shields.io/badge/release-1.0-blue.svg)](https://github.com/latidude99/enquiries/tree/master/release)
[![License: GPL v3](https://img.shields.io/badge/license-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![GitHub issues](https://img.shields.io/badge/issues-open%200-greenred.svg)](https://GitHub.com/latidude99/enquiries/issues/)
[![](https://img.shields.io/badge/%20$%20-buy%20me%20a%20coffe-yellow.svg)](https://www.buymeacoffee.com/zWn1I6bVf)

A tool for compressing files and folders to ZIP format with a simple JavaFX GUI.  
  
The idea is when you have a folder based project you working on frequiently and want 
to have a quick backup at the end of a day you drop this app in said folder, configure exluded files once and run it.   



# General Info

FolderBackupUtilFX packs the content of the folder it is executed from
(files, folders and subfolders) into a ZIP file. You can exclude
 files and folders listing them in the  `.config` file (which 
if exists is read at the application start). The `.config` file contains explanations 
how to use it.

When started the app will begin compressing the all folders, subfolders and files in the root folder showing
a progress bar in the main window. At any point the process can be stopped. When finished, cancelled or interrupted otherwise an option to delete the created ZIP archive will be shown as well as an option to close the app.
  
  
The content of the result  ZIP file includes (unless excluded explicitly in `.config`) the original `FolderBackupToolFX_vXX.jar` and `FolderBackupToolFX_vXX.config` files.   
    

By default the ZIP file name is composed of: `folder name` + `_backup_` + `date and time` + `.zip` 
(eg. `FolderBackupUtilFX_backup_2019-07-01 10.51.zip`) unless configured otherwise in the `.config` file.



# Status

Working. Not finished.

# Technologies
- Java JDK 8
- JavaFX 8
- Maven 3.60
- IntelliJ IDEA. 

**Plugins and libraries**
- Apache Shade Maven plugin  version 2.4.1
- Launch4j version 1.7.25
- Apache CommonsIO version 2.6


# License
FolderBackupUtilFX is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License 
as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
FolderBackupUtilFX is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 
warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with FolderBackupUtilFX. 
If not, see http://www.gnu.org/licenses/ or write to: latidude99@gmail.com

# Contact
You can email me at latidude99@gmail.com

