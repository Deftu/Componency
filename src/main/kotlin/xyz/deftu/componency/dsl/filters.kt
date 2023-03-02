package xyz.deftu.componency.dsl

import xyz.deftu.componency.components.BaseComponent
import xyz.deftu.componency.filters.Filter

infix fun <T : BaseComponent> T.filter(filter: Filter) = apply {
    applyFilter(filter)
}
