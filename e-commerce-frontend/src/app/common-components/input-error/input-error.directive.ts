import { Directive, Optional, Self } from "@angular/core";
import { NgControl } from "@angular/forms";

@Directive({
  selector: "[inputError]",
  standalone: true,
})
export class InputErrorDirective {
  constructor(@Optional() @Self() public ngControl: NgControl) {}
}
