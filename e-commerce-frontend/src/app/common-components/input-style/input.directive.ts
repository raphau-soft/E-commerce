import { Directive, HostBinding } from "@angular/core";

@Directive({
  selector: "[inputStyle]",
  standalone: true,
})
export class InputStyleDirective {
  @HostBinding("class") get classes(): string {
    return "input";
  }
}
