# SCENARIOS TO RUN

All the commands you will have to run to execute these scenarios must be written in the client CLI.
It is better to prepare terminals attached to the "bank-service", "mail-service" and "display-service"
to be able to notice changes or proofs of communication.

The commands correspond to all the following formatted text looking like this:

> - `commandName params...`

## Launching the automatic demo
We prepared an automatic demo which populates the DB with a resort, ski lifts, items in the catalog
and few customers buying cards and passes and checking their cards to some ski lifts.

You will first have to log in as an employee, so that the demo knows its commands, and then launch it:

> - `login root`
> - `runDemo`

You should see all the demo's commands being executed.


## Buying a new pass to link it to an already bought card

The user Ryana Karaki (has been set by the automatic demo) will now log into her account:

> - `login ryana.karaki@poly.fr`
> - `displayCards`

We can see here that she has a physical card with id "0112358132134558" which is a simple card named "Cartex-demo".\
But, she also has the same simple card that she has not picked up yet. So she will browse the catalog and
decide to buy 2 "Expert-demo" passes, one lasting 120 hours for her and one lasting 72 hours for her child.

> - `displayCatalog`
> - `orderPasses Expert-demo 120 false 1`
> - `orderPasses Expert-demo 72 true 1`

She will then pay her cart and disconnect.

> - `validateCart`
> - `exit`

## Going to retrieve the card at a resort

Firstly, Ryana goes to see the resort reception and, a CastexSki employee gives her a new physical card
of number "0102030403020100" and links it to the "Cartex-demo" card she bought online.

> - `linkCard ryana.karaki@poly.fr Cartex-demo 0102030403020100`

Ryana will then connect to her account and display her cards to see that her new card is well linked.\
And after that she decides to link the pass she recently bought to that card.

> - `login ryana.karaki@poly.fr`
> - `displayCards`
> - `linkPass Expert-demo 120 false 0102030403020100`
> - `exit`

## A little ski session

Our user decides to take the first ski lift she found which is the lift "lift_2":

> - `checkCard resort_F lift_2 0102030403020100`

The problem is that she forgot this lift was not accessible with her pass, so she walks to the next lift "lift_3".\
Meanwhile, an employee decide to check the stats at the lift "lift_3" and sees Ryana's access in real time.\
<You need to specify the date and hour (UTC) in the following commands (default is the 09-05-2021 at 20)>

> - `presenceHourStat resort_F lift_3 2021 05 09 20`
> - `checkCard resort_F lift_3 0102030403020100`
> - `presenceHourStat resort_F lift_3 2021 05 09 20`

## Overloaded ski lift notification

The ski lift "d_lift_2" is a ski lift using a double check system with an overloaded threshold at 1min.
Earlier during the automatic demo, someone checked the SuperCartex "0123456789101112" to that lift only once.
Therefore, to get an overloading notification, we can just check it once again to slow the traffic at "d_lift_2"
(if you waited at least 1min before executing the following command).

> - `checkCard resort_F d_lift_2 0123456789101112`

You should soon see a notification in the "mail-service" saying that the lift is overloaded.

## SuperCartex discounts (need time management)

As it is said in the instructions, the first hour of a superCartex must be free. So all the superCartex owners that
checked since you launched the CLI didn't pay. Therefore, if you can fast-forward the clock of the CLI
(`date +%T -s "hh:mm:ss"` in a docker image run with user privilege should work), you can test:

> - `checkCard resort_F lift_5 0123456789101112`

In the "bank-service" we can see that the owner paid 1 for that lift.
Now an employee adds a new discount for all the superCartex to be free today and after checking again the previous
card to the lift, no payment will be done.\
<You need to specify the current day into the name of the discount to make it efficient (default is 09-05-2021)>

> - `addEntryPass SUPER_DISCOUNT_09-05-2021 0 0 1 true`
> - `checkCard resort_F lift_5 0123456789101112`

## Closing the resort

Finally, after a long day, we can now close the resort and display on the signs that it is closed.

> - `closeResort resort_F`
> - `displaysText resort_F "The resort is currently closed"`

You can check the "display-service" to see the signs messages.\
if someone then tries to check a superCartex like before, it will not work.

> - `checkCard resort_F lift_5 0123456789101112`

## Cleaning the demo and exit

You can now clean-up the all demo to exit or stay in that state to execute the commands you want ("?" for help).

> - `exit`

It removes the resort, all the users and all the catalog entries except the one you added.\
You end up in the original client CLI as an employee: `exit` twice to quit.
