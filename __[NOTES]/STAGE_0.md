### [STAGE 0]: Basic engine setup

* [X] Basic ECS implementation with components
* [x] Basic OpenGL 2D line renderer
* [x] Basic player input (register key presses)
* [x] Basic window to display current features
* [x] Stage_0 example app that displays the frame of the projection area

### [STAGE 1]: Basic rendering and input

* [x] Basic compilation and linking shader system
* [x] API agnostic keyboard inputs
* [x] Basic texture rendering
* [x] Basic asset loading
> _[08/10/25]_ Due to the fact that is needed to load a texture to be able to render it, the asset management side of 
things has been slightly forwarded. Textures should be handled and loaded completely separately from the renderer, as 
to allow different render APIs to use the same assets and engine parts that are technically graphics API agnostic
* [x] Stage_1 example app that draws a triangle with blended vertex colors and an example of textured rendering


### [STAGE_2]: PROJECTION TIEM

* [ ] Camera zoom and projection control
* [ ] Transformations
* [ ] example app adapted to use and control projection, with transformed objects from the previous example, that can be
controlled by keyboard inputs.