


### demo2
![CoordinatorLayout](https://raw.githubusercontent.com/ooftf/Material/master/img/blog/ow8jg-6rpdu.gif)


### demo3
是仿京东和淘宝的商品详情页面

具体方案是，最外层使用 RecyclerView ，内层使用 ViewPager 作为 RecyclerView 最后一个 item，ViewPager 的高度与 RecyclerView 相同
ViewPager 的 item 再用一个 内层 RecyclerView 实现；

这个方案，最开始会有只有外层 RecyclerView 可以滑动，内层 RecyclerView 无法滑动；解决方案是 使用 NestedScroll 机制

### demo4
一种 RecyclerView  item 吸顶的实现方案