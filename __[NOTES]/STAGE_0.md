### [UPDATE_0]: Basic engine setup

* [X] Basic ECS implementation with components
* [x] Basic OpenGL 2D line glRenderer
* [x] Basic player input (register key presses)
* [x] Basic window to display current features
* [x] Stage_0 example app that displays the frame of the projection area

### [UPDATE_1]: Basic rendering and input

* [x] Basic compilation and linking shader system
* [x] API agnostic keyboard inputs
* [x] Basic texture rendering
* [x] Basic asset loading
> _[08/10/25]_ Due to the fact that is needed to load a texture to be able to render it, the asset management side of 
things has been slightly forwarded. Textures should be handled and loaded completely separately from the glRenderer, as 
to allow different render APIs to use the same assets and engine parts that are technically graphics API agnostic
* [x] Stage_1 example app that draws a triangle with blended vertex colors and an example of textured rendering

### [UPDATE_2]: PROJECTION TIEM

* [ ] Camera zoom and projection control
* [ ] Transformations
* [x] example app adapted to use and control projection, with transformed objects from the previous example, that can be
controlled by keyboard inputs.

### [UPDATE_2]: LETS GET SERIOUS

* [ ] Engine event system. First made to implement and propagate input controls agnostic of window API
* [ ] Integration with Alma. Implement Engine APIs to create and manage entities from an interface, allowing different entity managers to be used.
* [ ] Entity factory Stage 1. Create and manage entities through an API. Initially will only support Transform, Sprites, and input Listeners
* [ ] Asset manager Stage 1. Integrate and implement Shader and texture management