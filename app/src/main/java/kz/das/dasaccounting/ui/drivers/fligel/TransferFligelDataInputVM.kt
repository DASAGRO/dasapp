package kz.das.dasaccounting.ui.drivers.fligel

import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.data.office.NomenclatureOfficeInventory
import org.koin.core.inject

class TransferFligelDataInputVM: BaseVM() {

    private val officeInventoryRepository: OfficeInventoryRepository by inject()

    private val nomenclatures: ArrayList<NomenclatureOfficeInventory> = arrayListOf()

    fun setNomenclatures(list: List<NomenclatureOfficeInventory>) {
        nomenclatures.clear()
        nomenclatures.addAll(list)
    }

    fun getNomenclatures() = nomenclatures

    fun getNomenclaturesLocally() = officeInventoryRepository.getNomenclaturesLocally()

}