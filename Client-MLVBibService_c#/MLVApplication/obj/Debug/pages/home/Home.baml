   M S B A M L   `   `   `  ����I  EMLVApplication, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nullQ-clr-namespace:FirstFloor.ModernUI.App.ContentFirstFloor.ModernUI.App.Content  5      A%clr-namespace:FirstFloor.ModernUI.AppFirstFloor.ModernUI.App  5      M IWpfPageTransitions, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nullS<clr-namespace:WpfPageTransitions;assembly=WpfPageTransitionsWpfPageTransitions 5      /clr-namespace:MLVApplicationMLVApplication  5
          MLVApplication.pages.Home   5      \ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5      R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5      Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5      @d2http://schemas.microsoft.com/expression/blend/2008     5      Acontent-clr-namespace:FirstFloor.ModernUI.App.Content     5      5app%clr-namespace:FirstFloor.ModernUI.App     5      XpageTransitions<clr-namespace:WpfPageTransitions;assembly=WpfPageTransitions     5      .localclr-namespace:MLVApplication     5
      Z VFirstFloor.ModernUI, Version=1.0.6.0, Culture=neutral, PublicKeyToken=2d21ec3cd074c59a0mui&http://firstfloorsoftware.com/ModernUI  5        � DataContext  5      �� *
Rubrique+   Locator �� Source# [  .��5      �   ContentRoot#��[ 6   .+�5   
   �   HeaderMargin#��[ 6   $��Right=�66   $	��Top=�6R   .+�5      �� �� 5       �� Orientation$ 
Horizontal=�6   .+�5      �� $
��Left=�6   $ 
Horizontal=�68    � 	Resources 5      7  1FirstFloor.ModernUI.Windows.Controls.ModernButton �� 
TargetType �� Property �� Value��%z   '        �� 5      "  6    .�5      ��  � ��$ 0,0,8,0q�67   �� 5       � ��$ 2q�6@   5      5      .+�5      '  !WpfPageTransitions.PageTransition  -   5        $��
transition��5   R      TransitionType$ SpinAndFade=�65   $��30��6d   $��2q�6p   �� 5       -   5         ��Name$
 Page��5       $	��200��6-   $��30��69   $��2q�6E   ��6P   �� *Pages+	 �� SelectedItem	 6o   �� *SelectedPage+�� 5"      -   5        $��search��5"   B   $	��250��6   $��30��6+   $��2q�67   .��6P   5$      �� 5%      -   5        $��b1��5%   0   $��Search��6   $��2q�6:   6E   5&      5'      5)      � 5+   
   #��[ 6   $
��Left=�66   $	��Top=�6Q   .+�5,      �� �� 5-        Heading2#��[ 6   
Rubrique6E   5.      4  .FirstFloor.ModernUI.Windows.Controls.ModernTab  50   
   -   5        $
��menu��50   r   
   Layout$

 List=�6      SelectedSource$&  /Content/BookListTransition.xaml?�6?      Links 6'   �� *Links+5<      5>      5?      