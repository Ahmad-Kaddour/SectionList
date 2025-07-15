package com.ahmadkaddour.sectionlist.model

import org.jetbrains.compose.resources.DrawableResource
import section_list_project.sample.composeapp.generated.resources.Res
import section_list_project.sample.composeapp.generated.resources.img_vendor_item_ex_1
import section_list_project.sample.composeapp.generated.resources.img_vendor_item_ex_2
import section_list_project.sample.composeapp.generated.resources.img_vendor_item_ex_3
import section_list_project.sample.composeapp.generated.resources.img_vendor_item_ex_4
import section_list_project.sample.composeapp.generated.resources.img_vendor_item_ex_5
import section_list_project.sample.composeapp.generated.resources.img_vendor_item_ex_6

data class CategoryItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: String,
    val image: DrawableResource
)

object CategoryFactory {
    val categories: List<Category>
        get() = listOf(
            Category(
                title = "Grilled",
                items = listOf(
                    CategoryItem(
                        id = 1,
                        name = "Grilled Chicken",
                        description = "Juicy grilled chicken seasoned with garlic, herbs, and a hint of lemon. Perfectly seared for a crispy skin and tender inside.",
                        price = "$9.99",
                        image = Res.drawable.img_vendor_item_ex_1
                    ),
                    CategoryItem(
                        id = 2,
                        name = "Grilled Vegetables",
                        description = "A medley of zucchini, bell peppers, mushrooms, and onions lightly brushed with olive oil and chargrilled for a smoky flavor.",
                        price = "$7.49",
                        image = Res.drawable.img_vendor_item_ex_2
                    ),
                    CategoryItem(
                        id = 3,
                        name = "Grilled Steak",
                        description = "A thick-cut sirloin steak, marinated overnight and grilled to your preferred doneness. Served with chimichurri sauce on the side.",
                        price = "$14.99",
                        image = Res.drawable.img_vendor_item_ex_5
                    )
                )
            ),
            Category(
                title = "Sandwiches",
                items = listOf(
                    CategoryItem(
                        id = 4,
                        name = "Club Sandwich",
                        description = "Triple-layered sandwich stacked with roasted chicken, crispy bacon, fresh lettuce, tomato, and creamy mayo on toasted bread.",
                        price = "$6.99",
                        image = Res.drawable.img_vendor_item_ex_5
                    ),
                    CategoryItem(
                        id = 5,
                        name = "Veggie Sandwich",
                        description = "A fresh and healthy choice with avocado, cucumber, tomato, lettuce, sprouts, and hummus on multigrain bread.",
                        price = "$5.49",
                        image = Res.drawable.img_vendor_item_ex_3
                    ),
                    CategoryItem(
                        id = 6,
                        name = "Turkey Sandwich",
                        description = "Smoked turkey breast layered with cheddar cheese, fresh lettuce, pickles, and mustard. Served on your choice of bread.",
                        price = "$6.49",
                        image = Res.drawable.img_vendor_item_ex_4
                    )
                )
            ),
            Category(
                title = "Pizza",
                items = listOf(
                    CategoryItem(
                        id = 7,
                        name = "Margherita",
                        description = "A traditional Italian pizza with a thin crust, fresh tomato sauce, creamy mozzarella, and fragrant basil leaves.",
                        price = "$8.99",
                        image = Res.drawable.img_vendor_item_ex_6
                    ),
                    CategoryItem(
                        id = 8,
                        name = "Pepperoni",
                        description = "Our best-seller! Loaded with spicy pepperoni slices and a generous layer of bubbling mozzarella on a golden crust.",
                        price = "$9.49",
                        image = Res.drawable.img_vendor_item_ex_4
                    ),
                    CategoryItem(
                        id = 9,
                        name = "BBQ Chicken",
                        description = "Grilled chicken breast pieces tossed in tangy barbecue sauce, with red onions and mozzarella on a smoky BBQ base.",
                        price = "$10.49",
                        image = Res.drawable.img_vendor_item_ex_1
                    )
                )
            )
        )
}