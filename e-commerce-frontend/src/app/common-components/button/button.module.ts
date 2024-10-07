import { NgModule } from "@angular/core";
import {
  PrimaryButtonDirective,
  SecondaryButtonDirective,
} from "./button.directive";

@NgModule({
  declarations: [PrimaryButtonDirective, SecondaryButtonDirective],
  imports: [],
  exports: [PrimaryButtonDirective, SecondaryButtonDirective],
})
export class ButtonModule {}
