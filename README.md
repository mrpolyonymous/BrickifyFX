# BrickifyFX

A program to turn images in to mosaics you can build with your favourite plastic brick construction toy.
By which I of course mean [LEGO](http://www.lego.com) because what other other brand is worth playing with?

This program is a fork of the GPL-licensed [PicToBrick](http://www.pictobrick.de), written by
Tobias Reichling and Adrian Schütz. Due to the nature of the GPL this program is also GPL.

BrickifyFX was mostly an exercise in learning a bit about JavaFX. I also found the UI of PicToBrick to be a bit quirky so
I wanted to re-do it. I don't claim this UI is superior but it works much better for me. PicToBrick has many more features and has been
translated in to several languages so it may serve you better.

## Requirements

BrickifyFX requires Java 8 and a windowing system to run. Java has been getting better about not screwing up your browser in
recent releases but it's still hard to recommend non-developers install it. If you already have Java 8 hanging around,
though, there's no reason not to give BrickifyFX a try.

## Running

In Windows you can probably double-click brickifyfx-1.0.jar to run the pre-built application.

In Eclipse or IDEA, `brickifyfx.BrickifyFX` has a `main` method that will launch the application. NetBeans used to have
some huge wrapper stuff around JavaFX applications. This may or may not still be the case; I haven't used NetBeans in a few
releases.

Eclipse still doesn't seem to understand that it's okay to call JavaFX APIs and generates warnings when you do.
The Eclipse project settings here have added an explicit rule to allow JavaFX API access. If these don't take, you can
disable warnings by going to Project properties->Java Build Path->Libraries->JRE System Library->Access rules, click Edit...,
then add a rule to make `javafx/**` accessible.
