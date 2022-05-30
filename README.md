# App behavior
The display of data in the app is online only (needs internet connection) to display the list of product, but the cart has full off line support. If there is no data the list will show an empty message, if there is no internet while loading data from the network a message about it will appear.

The list of products in the cart and all the info in the cart have live updates from the database, this way when an object is deleted it will update the list and summary automatically. The user is able to cancel or place an order, both operation will clear the cart, the user is also able to delete products by swiping items from the list.

Both lists (cart and main) have a ListAdapter with DiffUtil to efficiently perform the updates and to show smooth animation when deleting products.

**Discounts** can be modified out side of the app context (in firebase) but the app is limited to 2 types of discount according to the challenge.

# App arch

I tried to follow Clean Architecture principles as much as I could between presentation, domain and data packages. For simplicity the remote and local layers are inside the same package. The main architecture of the app is MVVM

Managed the dependency injection with Hilt. I used Retrofit to load data from the network and Room to store data locally, both queries and requests are used with coroutines to perform the operation in background. Also used Arrow lib to handle responses with either a failure or valid result, I created my own failure objects, there are also event objects, the idea is that these failure/event objects can have meta-data to pass to the presentation layer or the fragment depending on what the error/event is, this way is easier to provide feedback to the user.

There is a base fragment to perform operations that are to be used in most fragments of the app, in this case the loading state, there is also a baseViewModel that handle failures/events that can be used to log crashes by sending throwable or exception if the failure object accepts them as param. The idea of both is to have common blocks of code to reuse

**Discounts** are created using firebase remote config, I used the following format for each discount, this way we can modify them:

```json
{
  "codes": [
    "TSHIRT"
  ],
  "reduce_price_by": 1,
  "min_valid_quantity": 3,
  "items_to_apply": 0,
  "label": "Bulk discount",
  "message": "Buy 3 or more and get a discount of 1 euro on each"
}
```
Each discount can be applied to as many items as we want through the `codes` array, but only one can be applied, Multi Buy Discount has priority over the Bulk Discount that means that if an items has both discount only Multi Buy will be applied, this is set through code, if that's to be changed the app needs to be released again with a new build if new discounts need to be created a new build will be needed as well. The difference between discounts format in the json is that Multi Buy Discount has a `discountPercent` prop, with this we can set the item to be free or discounted by a certain percentage, and Bulk Discount has a `reduce_price_by` prop that allows price reduction by currency. `label` prop is the discount name displayed in the main list, `message` prop is the description or rules of the discount, `min_valid_quantity` is the min needed of items in an order for the discount to be applied, `items_to_apply` is the amount of items that the discount needs to be applied to (in Bulk Discount if this is 0 it means that the discount will be applied to all items of that type in the order.

