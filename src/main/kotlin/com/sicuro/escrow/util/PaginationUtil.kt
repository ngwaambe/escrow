package com.sicuro.escrow.util

import com.sicuro.escrow.model.SortOrder
import org.springframework.data.domain.Sort

class PaginationUtil {

    companion object {

        fun getPage(offset:Int, pageSize: Int): Int = if (offset == 0) 0 else (offset/pageSize) as Int

        fun sorting(sortOrder: SortOrder?, sortingField: String?): Sort {
            return if(!sortingField.isNullOrEmpty()){
                Sort.by(sortDirection(sortOrder),  sortingField)
            } else {
                Sort.by(Sort.Direction.DESC, "lastModified", "created")
            }
        }

        private fun sortDirection(sortOrder: SortOrder?): Sort.Direction =
            when(sortOrder) {
                SortOrder.ASCENDING -> Sort.Direction.ASC
                SortOrder.DESCENDING -> Sort.Direction.DESC
                else -> Sort.DEFAULT_DIRECTION
            }
    }
}
