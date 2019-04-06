package pl.dzazef.todo.activities

import pl.dzazef.todo.data.Item

interface ActivityInterface {
    fun showDetailActivity(dateAndTime: String, message: String, color: Int, priority: String)
    fun setRecyclerView()
    fun addItemToView(item: Item)
}