package com.everwing.coreservice.common.wy.service.cust.person;

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNewImportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;

import java.util.List;

public interface PersonCustImportService {

    MessageMap doImport(WyBusinessContext ctx, TSysImportExportSearch tSysImportExportRequest);

    MessageMap importPerson(WyBusinessContext ctx, List<PersonCustNewImportList> personCustNewImportLists);
}
